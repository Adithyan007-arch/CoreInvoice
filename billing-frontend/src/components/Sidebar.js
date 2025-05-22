import { Link, useLocation } from 'react-router-dom';
import {
  FaHome,
  FaFileInvoice,
  FaBox,
  FaUsers,
  FaChartBar,
  FaCog,
  FaSignOutAlt,
  FaPlus,
  FaEdit,
  FaDownload
} from 'react-icons/fa';

const Sidebar = () => {
  const location = useLocation();

  const menu = [
    { icon: <FaHome />, label: 'Dashboard', path: '/' },
    { icon: <FaFileInvoice />, label: 'Invoices', path: '/invoices' },
    {
      icon: <FaBox />,
      label: 'Products',
      path: '/products',
      children: [
        { icon: <FaPlus className="text-sm" />, label: 'Add Product', path: '/products/add' },
        { icon: <FaEdit className="text-sm" />, label: 'Edit Product', path: '/products/edit' }
      ]
    },
    { icon: <FaUsers />, label: 'Customers', path: '/customers' },
    {
      icon: <FaChartBar />,
      label: 'Reports',
      path: '/reports',
      children: [
        {
          icon: <FaDownload className="text-sm" />,
          label: 'Download CSV',
          path: '/api/reports/export'
        }
      ]
    },
    { icon: <FaCog />, label: 'Settings', path: '/settings' },
  ];

  const isActive = (path) =>
    location.pathname === path || location.pathname.startsWith(path + '/');

  return (
    <div className="w-64 bg-gray-900 text-white min-h-screen p-6 flex flex-col justify-between">
      <div>
        <h1 className="text-2xl font-bold mb-8">Billing System</h1>
        <ul className="space-y-4">
          {menu.map((item, idx) => (
            <li key={idx}>
              <Link
                to={item.path}
                className={`flex items-center gap-4 hover:text-blue-400 ${
                  isActive(item.path) && !item.children
                    ? 'text-blue-400 font-semibold'
                    : ''
                }`}
              >
                {item.icon}
                {item.label}
              </Link>

              {item.children && (
                <ul className="ml-8 mt-2 space-y-2 text-sm">
                  {item.children.map((subItem, subIdx) => (
                    <li key={subIdx}>
                      <Link
                        to={subItem.path}
                        className={`flex items-center gap-2 hover:text-blue-300 ${
                          location.pathname === subItem.path
                            ? 'text-blue-300 font-semibold'
                            : ''
                        }`}
                      >
                        {subItem.icon}
                        {subItem.label}
                      </Link>
                    </li>
                  ))}
                </ul>
              )}
            </li>
          ))}
        </ul>
      </div>
      <button className="flex items-center gap-2 text-sm mt-10 hover:text-red-400">
        <FaSignOutAlt />
        Logout
      </button>
    </div>
  );
};

export default Sidebar;