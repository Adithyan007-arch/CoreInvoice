import { useEffect, useState } from "react";

const CustomerPage = () => {
  const [customers, setCustomers] = useState([]);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [editId, setEditId] = useState(null);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');

  const fetchCustomers = () => {
    fetch("http://localhost:8081/customers")
      .then(res => res.json())
      .then(setCustomers);
  };

  useEffect(() => {
    fetchCustomers();
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();
    const method = editId ? 'PUT' : 'POST';
    const url = editId ? `http://localhost:8081/customers/${editId}` : `http://localhost:8081/customers`;
    {success && <p className="text-green-600 mb-2">Customer added successfully!</p>}
    {error && <p className="text-red-600 mb-2">{error}</p>}

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email })
    }).then(() => {
      setName('');
      setEmail('');
      setEditId(null);
      fetchCustomers();
    });
  };

  const handleEdit = (customer) => {
    setEditId(customer.id);
    setName(customer.name);
    setEmail(customer.email);
  };

  const handleDelete = (id) => {
    fetch(`http://localhost:8081/customers/${id}`, {
      method: 'DELETE'
    }).then(fetchCustomers);
  };

  return (
    <div className="p-6 max-w-3xl mx-auto">
      <h2 className="text-2xl font-bold mb-4">Customer Management</h2>

      <form onSubmit={handleSubmit} className="mb-6 space-y-4">
        <input
          type="text"
          placeholder="Customer name"
          value={name}
          required
          onChange={e => setName(e.target.value)}
          className="w-full border px-3 py-2 rounded"
        />
        <input
          type="email"
          placeholder="Customer email"
          value={email}
          required
          onChange={e => setEmail(e.target.value)}
          className="w-full border px-3 py-2 rounded"
        />
        <button
          type="submit"
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          {editId ? "Update Customer" : "Add Customer"}
        </button>
      </form>

      <table className="w-full border">
        <thead className="bg-gray-200">
          <tr>
            <th className="p-2 text-left">Name</th>
            <th className="p-2 text-left">Email</th>
            <th className="p-2">Actions</th>
          </tr>
        </thead>
        <tbody>
          {customers.map(c => (
            <tr key={c.id} className="border-t">
              <td className="p-2">{c.name}</td>
              <td className="p-2">{c.email}</td>
              <td className="p-2 flex gap-2 justify-center">
                <button onClick={() => handleEdit(c)} className="text-blue-600">Edit</button>
                <button onClick={() => handleDelete(c.id)} className="text-red-600">Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default CustomerPage;