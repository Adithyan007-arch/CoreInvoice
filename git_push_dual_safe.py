import os
import sys
import subprocess

def run_cmd(cmd):
    result = subprocess.run(cmd, shell=True, text=True, capture_output=True)
    if result.returncode != 0 and "nothing to commit" not in result.stderr:
        print(f"❌ Error: {result.stderr}")
        sys.exit(1)
    return result.stdout.strip()

def commit_and_push(branch, message):
    print(f"\n🔄 Switching to branch: {branch}")
    run_cmd(f"git checkout {branch} || git checkout -b {branch}")

    print(f"✅ Adding all changes...")
    run_cmd("git add .")

    print(f"📝 Committing: {message}")
    run_cmd(f'git commit -m "{message}"')

    print(f"🚀 Pushing to {branch}")
    run_cmd(f"git push origin {branch}")

def main():
    if len(sys.argv) < 2:
        print("Usage: python git_push_dual_safe.py \"commit message\"")
        sys.exit(1)

    commit_message = sys.argv[1]

    # Push to dev without asking
    commit_and_push("dev", commit_message)

    # Ask before pushing to prod
    confirm = input("\n⚠️ Do you want to push the same commit to `prod`? (yes/no): ").strip().lower()
    if confirm in ("yes", "y"):
        commit_and_push("prod", commit_message)
    else:
        print("🛑 Skipped pushing to `prod`.")

    print("\n✅ Done.")

if __name__ == "__main__":
    main()