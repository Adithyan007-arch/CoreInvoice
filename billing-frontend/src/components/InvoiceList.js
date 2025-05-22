import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

const InvoiceList = () => {
  const [invoices, setInvoices] = useState([]);

  useEffect(() => {
    fetch('http://localhost:8081/api/invoices')
      .then(res => res.json())
      .then(data => {console.log('Fetched Invoices:', data); setInvoices(data)})
      .catch(err => console.error('Failed to fetch invoices:', err));
  }, []);

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-2xl font-semibold">Invoices</h2>
        <Link
          to="/invoices/add"
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          + Add Invoice
        </Link>
      </div>

      <div className="overflow-x-auto">
        <table className="min-w-full bg-white border rounded shadow">
          <thead className="bg-gray-200">
            <tr>
              <th className="text-left px-4 py-2">Invoice ID</th>
              <th className="text-left px-4 py-2">Customer</th>
              <th className="text-left px-4 py-2">Date</th>
              <th className="text-left px-4 py-2">Total</th>
              <th className="text-left px-4 py-2">Actions</th>
            </tr>
          </thead>
          <tbody>
            {invoices.length > 0 ? (
              invoices.map((invoice) => (
                <tr key={invoice.id} className="border-t hover:bg-gray-50">
                  <td className="px-4 py-2">{invoice.id}</td>
                  <td className="px-4 py-2">{invoice.customer.name}</td>
                  <td className="px-4 py-2">{invoice.date}</td>
                  <td className="px-4 py-2">₹ {invoice.totalAmount}</td>
                  <td className="px-4 py-2">
                    <Link
                      to={`/invoices/${invoice.id}`}
                      className="text-blue-600 hover:underline"
                    >
                      View
                    </Link>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="5" className="px-4 py-4 text-center text-gray-500">
                  No invoices found.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default InvoiceList;