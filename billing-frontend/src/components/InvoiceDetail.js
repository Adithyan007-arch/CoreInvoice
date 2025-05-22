// src/pages/InvoiceDetail.jsx
import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';

const InvoiceDetail = () => {
  const { id } = useParams();
  const [invoice, setInvoice] = useState(null);

  useEffect(() => {
    fetch(`http://localhost:8081/api/invoices/${id}`)
      .then(res => res.json())
      .then(data => setInvoice(data))
      .catch(err => console.error('Failed to fetch invoice:', err));
  }, [id]);

  if (!invoice) return <div className="p-6">Loading invoice...</div>;

  return (
    <div className="p-6">
      <h2 className="text-2xl font-semibold mb-4">Invoice #{invoice.id}</h2>
      <p><strong>Customer:</strong> {invoice.customer?.name}</p>
      <p><strong>Email:</strong> {invoice.customer?.email}</p>
      <p><strong>Date:</strong> {invoice.date}</p>
      <p><strong>Total:</strong> ₹ {invoice.totalAmount}</p>
      <p><strong>Paid:</strong> {invoice.paid ? 'Yes' : 'No'}</p>

      <h3 className="mt-4 font-semibold">Items</h3>
      <ul className="list-disc ml-6">
        {invoice.items.map(item => (
          <li key={item.id}>
            ID: {item.id} — Qty: {item.quantity} — Price: ₹{item.price}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default InvoiceDetail;