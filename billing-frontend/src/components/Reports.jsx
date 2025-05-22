import { FaDownload } from 'react-icons/fa';

export default function Reports() {
  const downloadCSV = () => {
    window.open('/api/reports/export', '_blank');
  };

  return (
    <div className="p-6">
      <h2 className="text-xl font-bold mb-4">Reports</h2>
      <button
        onClick={downloadCSV}
        className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
      >
        <FaDownload />
        Export Full Data as CSV
      </button>
    </div>
  );
}
