import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

const ProductList = () => {
  const [products, setProducts] = useState([]);

  // Fetch products on load
  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/products');
      setProducts(res.data);
    } catch (err) {
      console.error('Failed to fetch products:', err);
    }
  };

  const deleteProduct = async (id) => {
    if (!window.confirm('Are you sure you want to delete this product?')) return;

    try {
      await axios.delete(`http://localhost:8080/api/products/${id}`);
      setProducts(products.filter(p => p.id !== id));
    } catch (err) {
      console.error('Delete failed:', err);
    }
  };

  return (
    <div>
      <div className="flex justify-between mb-4">
        <h2 className="text-xl font-bold">All Products</h2>
        <Link to="/add-product" className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
          Add Product
        </Link>
      </div>

      <table className="min-w-full bg-white rounded shadow">
        <thead className="bg-gray-200">
          <tr>
            <th className="text-left py-2 px-4">ID</th>
            <th className="text-left py-2 px-4">Name</th>
            <th className="text-left py-2 px-4">Price</th>
            <th className="text-left py-2 px-4">Category</th>
            <th className="text-left py-2 px-4">Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <tr key={product.id} className="border-t">
              <td className="py-2 px-4">{product.id}</td>
              <td className="py-2 px-4">{product.name}</td>
              <td className="py-2 px-4">₹{product.price}</td>
              <td className="py-2 px-4">{product.category}</td>
              <td className="py-2 px-4 space-x-2">
                <Link
                  to={`/edit-product/${product.id}`}
                  className="text-blue-600 hover:underline"
                >
                  Edit
                </Link>
                <button
                  onClick={() => deleteProduct(product.id)}
                  className="text-red-600 hover:underline"
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
          {products.length === 0 && (
            <tr>
              <td colSpan="5" className="text-center py-4 text-gray-500">
                No products found.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default ProductList;