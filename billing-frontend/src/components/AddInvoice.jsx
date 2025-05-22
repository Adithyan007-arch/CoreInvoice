import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const AddInvoice = () => {
  const [customers, setCustomers] = useState([]);
  const [products, setProducts] = useState([]);
  const [selectedCustomer, setSelectedCustomer] = useState('');
  const [items, setItems] = useState([{ productId: '', quantity: 1 }]);

  const navigate = useNavigate();

  // Fetch customers and products
  useEffect(() => {
    fetch('http://localhost:8081/customers')
      .then(res => res.json())
      .then(setCustomers);
  
    fetch('http://localhost:8081/products')
      .then(res => res.json())
      .then(setProducts);
  }, []);

  // Handle form changes
  const handleItemChange = (index, field, value) => {
    const updated = [...items];
    updated[index][field] = value;
    setItems(updated);
  };

  const addItem = () => {
    setItems([...items, { productId: '', quantity: 1 }]);
  };

  const removeItem = (index) => {
    const updated = items.filter((_, i) => i !== index);
    setItems(updated);
  };

  const calculateTotal = () => {
    return items.reduce((total, item) => {
      const product = products.find(p => p.id === parseInt(item.productId));
      return total + (product ? product.price * item.quantity : 0);
    }, 0).toFixed(2);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const payload = {
      customerId: selectedCustomer,
      items: items.map(item => ({
        productId: parseInt(item.productId),
        quantity: parseInt(item.quantity)
      }))
    };

    fetch('http://localhost:8081/api/invoices', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      })
      .then(res => {
        if (res.ok) navigate('/invoices');
        else throw new Error('Failed to add invoice');
      })
      .catch(err => console.error(err));
  };

  return (
    <div className="p-6 max-w-3xl mx-auto">
      <h2 className="text-2xl font-semibold mb-6">Add Invoice</h2>

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Select Customer */}
        <div>
          <label className="block mb-1 font-medium">Customer</label>
          <select
            required
            value={selectedCustomer}
            onChange={e => setSelectedCustomer(e.target.value)}
            className="w-full border px-4 py-2 rounded"
          >
            <option value="">Select customer</option>
            {customers.map(c => (
              <option key={c.id} value={c.id}>{c.name}</option>
            ))}
          </select>
        </div>

        {/* Product Line Items */}
        <div>
          <label className="block mb-2 font-medium">Products</label>
          {items.map((item, idx) => (
            <div key={idx} className="flex gap-4 mb-2 items-center">
              {/* Product dropdown */}
              <select
                required
                value={item.productId}
                onChange={e => handleItemChange(idx, 'productId', e.target.value)}
                className="w-1/2 border px-2 py-1 rounded"
              >
                <option value="">Select product</option>
                {products.map(p => (
                  <option key={p.id} value={p.id}>
                    {p.name} (₹{p.price})
                  </option>
                ))}
              </select>

              {/* Quantity */}
              <input
                type="number"
                min="1"
                value={item.quantity}
                onChange={e => handleItemChange(idx, 'quantity', e.target.value)}
                className="w-1/4 border px-2 py-1 rounded"
              />

              {/* Remove Item */}
              {items.length > 1 && (
                <button type="button" onClick={() => removeItem(idx)} className="text-red-600 font-bold">
                  ✕
                </button>
              )}
            </div>
          ))}

          {/* Add Item */}
          <button
            type="button"
            onClick={addItem}
            className="mt-2 text-blue-600 hover:underline"
          >
            + Add Item
          </button>
        </div>

        {/* Total and Submit */}
        <div className="flex justify-between items-center pt-4">
          <p className="text-lg font-semibold">Total: ₹ {calculateTotal()}</p>
          <button
            type="submit"
            className="bg-green-600 text-white px-6 py-2 rounded hover:bg-green-700"
          >
            Save Invoice
          </button>
        </div>
      </form>
    </div>
  );
};

export default AddInvoice;