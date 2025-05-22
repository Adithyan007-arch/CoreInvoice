import { useEffect, useState } from 'react';
import axios from 'axios';

const ProductList = () => {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:8081/products')
      .then(res => setProducts(res.data))
      .catch(err => console.error('Error fetching products:', err));
  }, []);

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">All Products</h2>
      <table className="min-w-full bg-white rounded-lg shadow">
        <thead>
          <tr className="bg-gray-200 text-left">
            <th className="py-2 px-4">ID</th>
            <th className="py-2 px-4">Name</th>
            <th className="py-2 px-4">Price</th>
            <th className="py-2 px-4">Category</th>
            <th className="py-2 px-4">GST</th>
            <th className="py-2 px-4">Total</th>
          </tr>
        </thead>
        <tbody>
          {products.map(product => (
            <tr key={product.id} className="border-t">
              <td className="py-2 px-4">{product.id}</td>
              <td className="py-2 px-4">{product.name}</td>
              <td className="py-2 px-4">₹ {product.price}</td>
              <td className="py-2 px-4">{product.category}</td>
              <td className="py-2 px-4">{product.gstpercentage}%</td>
              <td className="py-2 px-4">₹ {product.final_amount}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ProductList;
