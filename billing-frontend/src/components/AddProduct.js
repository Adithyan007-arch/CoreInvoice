import { useState } from 'react';
import axios from 'axios';

const AddProduct = () => {
  const [form, setForm] = useState({
    name: '',
    price: '',
    category: '',
    gstpercentage: '',
    final_amount: ''
  });

  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await axios.post('http://localhost:8081/products', form);
      setSuccess(true);
      setForm({ name: '', price: '', category: '', gstpercentage: '', final_amount: '' });
    } catch (err) {
      setError('Failed to add product');
    }
  };

  return (
    <div className="max-w-lg mx-auto bg-white p-6 rounded-xl shadow">
      <h2 className="text-xl font-bold mb-4">Add Product</h2>
      {success && <p className="text-green-600 mb-2">Product added successfully!</p>}
      {error && <p className="text-red-600 mb-2">{error}</p>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block font-medium">Name</label>
          <input
            name="name"
            value={form.name}
            onChange={handleChange}
            required
            className="w-full border rounded px-3 py-2 mt-1"
            placeholder="Product Name"
          />
        </div>
        <div>
          <label className="block font-medium">Price</label>
          <input
            name="price"
            type="number"
            value={form.price}
            onChange={handleChange}
            required
            className="w-full border rounded px-3 py-2 mt-1"
            placeholder="Price in ₹"
          />
        </div>
        <div>
          <label className="block font-medium">Category</label>
          <input
            name="category"
            value={form.category}
            onChange={handleChange}
            required
            className="w-full border rounded px-3 py-2 mt-1"
            placeholder="E.g. Electronics"
          />
        </div>
        <div>
          <label className="block font-medium">GST</label>
          <input
            name="gst"
            value={form.gstpercentage}
            onChange={handleChange}
            required
            className="w-full border rounded px-3 py-2 mt-1"
            placeholder="E.g. GST Percentage"
          />
        </div>
        <div>
          <label className="block font-medium">Total Amount</label>
          <input
            name="total"
            value={form.final_amount}
            onChange={handleChange}
            required
            className="w-full border rounded px-3 py-2 mt-1"
            placeholder="E.g. Total Amount"
          />
        </div>
        <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
          Add Product
        </button>
      </form>
    </div>
  );
};

export default AddProduct;