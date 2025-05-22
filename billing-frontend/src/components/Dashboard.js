import { useEffect, useState } from 'react';
import { getDashboardStats } from '../api/dashboardApi';
import {
  LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie, Cell
} from 'recharts';
import ClipLoader from 'react-spinners/ClipLoader';

const COLORS = ['#3b82f6', '#60a5fa', '#93c5fd'];

export default function Dashboard() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchData = () => {
    setLoading(true);
    getDashboardStats()
      .then(res => {
        console.log('Dashboard Data:', res.data);
        setData(res.data);
        setLoading(false);
      })
      .catch(err => {
        console.error('API error:', err);
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchData();
  }, []);

  if (loading || !data) {
    return (
      <div className="flex justify-center items-center h-64">
        <ClipLoader color="#3b82f6" size={40} />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-end">
        <button
          onClick={fetchData}
          className="mb-4 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
        >
          Refresh Stats
        </button>
      </div>

      <div className="grid grid-cols-3 gap-6">
        <div className="bg-white p-4 rounded-2xl shadow">
          <h2 className="text-sm text-gray-500">Total Revenue</h2>
          <p className="text-2xl font-bold mt-1">
            ₹{data.totalRevenue.toLocaleString(undefined, { minimumFractionDigits: 2 })}
          </p>
        </div>
        <div className="bg-white p-4 rounded-2xl shadow">
          <h2 className="text-sm text-gray-500">Total Invoices</h2>
          <p className="text-2xl font-bold mt-1">{data.invoiceCount}</p>
        </div>
        <div className="bg-white p-4 rounded-2xl shadow">
          <h2 className="text-sm text-gray-500">Customers</h2>
          <p className="text-2xl font-bold mt-1">{data.customerName}</p>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-6">
        <div className="bg-white p-4 rounded-2xl shadow">
          <h2 className="text-sm text-gray-500 mb-2">Monthly Revenue</h2>
          {data.totalMonthlyRevenue?.length > 0 ? (
  <ResponsiveContainer width="100%" height={200}>
    <LineChart data={data.totalMonthlyRevenue.map(m => ({
      name: `M${m.month}`,
      revenue: m.revenue
    }))}>
      <XAxis dataKey="name" />
      <YAxis />
      <Tooltip />
      <Line type="monotone" dataKey="revenue" stroke="#3b82f6" strokeWidth={2} />
    </LineChart>
  </ResponsiveContainer>
) : (
  <p className="text-gray-400">No monthly revenue data available</p>
)}
        </div>

        <div className="bg-white p-4 rounded-2xl shadow">
          <h2 className="text-sm text-gray-500 mb-2">Revenue by Category</h2>
          {data.totalRevenueByCategory?.length > 0 ? (
  <ResponsiveContainer width="100%" height={200}>
    <PieChart>
      <Pie
        data={data.totalRevenueByCategory.map(c => ({
          name: c.category,
          value: c.total
        }))}
        dataKey="value"
        nameKey="name"
        cx="50%"
        cy="50%"
        outerRadius={70}
        fill="#3b82f6"
        label
      >
        {data.totalRevenueByCategory.map((_, i) => (
          <Cell key={i} fill={COLORS[i % COLORS.length]} />
        ))}
      </Pie>
      <Tooltip />
    </PieChart>
  </ResponsiveContainer>
) : (
  <p className="text-gray-400">No category revenue data available</p>
)}
        </div>
      </div>

      <div className="bg-white p-4 rounded-2xl shadow">
        <h2 className="text-lg font-semibold mb-4">Recent Invoices</h2>
        {data.recentInvoices?.length > 0 ? (
          <table className="w-full text-sm">
            <thead>
              <tr className="text-left text-gray-600 border-b">
                <th className="py-2">Invoice</th>
                <th className="py-2">Customer</th>
                <th className="py-2">Amount</th>
                <th className="py-2">Status</th>
                <th className="py-2">Action</th>
              </tr>
            </thead>
            <tbody>
  {data.recentInvoices.map((invoice, i) => {
    const [invoiceId, customerName, amount, status] = invoice;
    return (
      <tr key={i} className="border-b hover:bg-gray-50">
        <td className="py-2">{invoiceId}</td>
        <td className="py-2">{customerName}</td>
        <td className="py-2">
          {amount != null ? `₹${amount.toLocaleString(undefined, { minimumFractionDigits: 2 })}` : 'N/A'}
        </td>
        <td className="py-2">
          <span className={`px-3 py-1 rounded-full text-xs font-semibold ${
            status ? 'bg-green-100 text-green-600' : 'bg-yellow-100 text-yellow-600'
          }`}>
            {status ? 'Paid' : 'Pending'}
          </span>
        </td>
        <td className="py-2">
          <button className="text-sm px-3 py-1 bg-gray-200 rounded-md">View</button>
        </td>
      </tr>
    );
  })}
</tbody>
          </table>
        ) : (
          <p className="text-gray-400">No recent invoices available</p>
        )}
      </div>
    </div>
  );
}