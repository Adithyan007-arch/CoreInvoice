import os
import sys
import subprocess
import shutil
import logging
from pathlib import Path
from tokenize import String

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

TEMP_UNTRACKED_DIR = ".tmp_untracked"

def run_cmd(cmd, ignore_error=False):
    result = subprocess.run(cmd, shell=True, text=True, capture_output=True)
    if result.returncode != 0:
        if ignore_error:
            return result.stdout.strip() + result.stderr.strip()
        logging.info(f"❌ Error: {result.stderr.strip()}")
        sys.exit(1)
    return result.stdout.strip()

def get_untracked_files():
    output = run_cmd("git ls-files --others --exclude-standard")
    return output.splitlines()

def stash_untracked_files():
    untracked_files = get_untracked_files()
    if untracked_files:
        logging.info("📁 Moving untracked files to temporary folder...")
        Path(TEMP_UNTRACKED_DIR).mkdir(exist_ok=True)
        for f in untracked_files:
            target_path = os.path.join(TEMP_UNTRACKED_DIR, f)
            os.makedirs(os.path.dirname(target_path), exist_ok=True)
            shutil.move(f, target_path)

def restore_untracked_files():
    if not os.path.exists(TEMP_UNTRACKED_DIR):
        return
    logging.info("📂 Restoring untracked files...")
    for root, _, files in os.walk(TEMP_UNTRACKED_DIR):
        for file in files:
            full_path = os.path.join(root, file)
            rel_path = os.path.relpath(full_path, TEMP_UNTRACKED_DIR)
            target_dir = os.path.dirname(rel_path)
            if target_dir:
                os.makedirs(target_dir, exist_ok=True)
            shutil.move(full_path, rel_path)
    shutil.rmtree(TEMP_UNTRACKED_DIR)

def safe_checkout(branch):
    logging.info(f"\n💾 Stashing committed changes...")
    run_cmd("git stash", ignore_error=True)

    stash_untracked_files()

    logging.info(f"🔀 Switching to branch: {branch}")
    checkout_result = subprocess.run(f"git checkout {branch}", shell=True, text=True, capture_output=True)

    if checkout_result.returncode != 0:
        if "did not match any file" in checkout_result.stderr.lower():
            logging.info(f"📦 Creating new branch: {branch}")
            run_cmd(f"git checkout -b {branch}")
        else:
            logging.info(f"❌ Error: {checkout_result.stderr.strip()}")
            restore_untracked_files()
            sys.exit(1)
    else:
        logging.info(checkout_result.stdout.strip())

    logging.info(f"🔄 Applying stashed changes...")
    run_cmd("git stash pop", ignore_error=True)
    restore_untracked_files()

def commit_and_push(branch, message):
    safe_checkout(branch)

    logging.info(f"✅ Adding all changes...")
    run_cmd("git add .")

    logging.info(f"📝 Committing with message: {message}")
    run_cmd(f'git commit -m "{message}"', ignore_error=True)

    logging.info(f"🚀 Pushing to {branch}...")
    run_cmd(f"git push origin {branch}")

def main():
    if len(sys.argv) < 2:
        logging.info("Usage: python git_push_dual_safe.py \"commit message\"")
        sys.exit(1)

    commit_message = sys.argv[1]

    logging.info("==== PUSHING TO DEV ====")
    commit_and_push("Dev", commit_message)

    print("\n==== CONFIRMATION REQUIRED FOR PROD ====")
    confirm = input("⚠️ Push to `Prod` as well? (yes/no): ").strip().lower()
    if confirm in ("yes", "y"):
        logging.info("\n==== PUSHING TO PROD ====")
        commit_and_push("Prod", commit_message)
    else:
        logging.info("🛑 Skipped pushing to `Prod`.")

    logging.info("✅ All operations completed successfully.")

if __name__ == "__main__":
    main()