import { useParams, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axios from 'axios';

const EditProductForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [product, setProduct] = useState({ name: '', price: '', category: '' });

  useEffect(() => {
    axios.get(`http://localhost:8081/products/${id}`)
      .then(res => setProduct(res.data))
      .catch(err => console.error('Failed to load product', err));
  }, [id]);

  const handleChange = (e) => {
    setProduct(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axios.put(`http://localhost:8081/products/${id}`, product)
      .then(() => {
        alert('Product updated successfully!');
        navigate('/products/edit'); // Go back to product list
      })
      .catch(err => alert('Failed to update product'));
  };

  return (
    <div className="p-6 max-w-xl mx-auto bg-gray-800 rounded text-white">
      <h2 className="text-xl font-bold mb-4">Edit Product</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <input type="text" name="name" placeholder="Name" value={product.name} onChange={handleChange}
          className="w-full p-2 rounded bg-gray-700" />
        <input type="number" name="price" placeholder="Price" value={product.price} onChange={handleChange}
          className="w-full p-2 rounded bg-gray-700" />
        <input type="text" name="category" placeholder="Category" value={product.category} onChange={handleChange}
          className="w-full p-2 rounded bg-gray-700" />
        <button type="submit" className="bg-blue-500 px-4 py-2 rounded hover:bg-blue-600">
          Update Product
        </button>
      </form>
    </div>
  );
};

export default EditProductForm;
