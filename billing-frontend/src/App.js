// src/App.js
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Dashboard from "./components/Dashboard";
import InvoiceList from "./components/InvoiceList";
import Products from "./components/Products";
import ProductList from './components/ProductList';
import AddProduct from './components/AddProduct';
import EditProduct from './components/EditProduct';
import AddInvoice from './components/AddInvoice';
import CustomerPage from './pages/CustomerPage';
import InvoiceDetail from './components/InvoiceDetail'; // <- create this next
import Reports  from "./components/Reports";  




function App() {
  return (
    <Router>
      <div className="flex">
        <Sidebar />
        <main className="flex-1 p-6">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/invoices" element={<InvoiceList />} />
            <Route path="/products" element={<ProductList />} />
            <Route path="/products/add" element={<AddProduct />} />
            <Route path="/edit-product/:id" element={<EditProduct />} />
            <Route path="/invoices/add" element={<AddInvoice />} />
            <Route path="/customers" element={<CustomerPage />} />
            <Route path="/invoices/:id" element={<InvoiceDetail />} />
            <Route path="/reports" element={<Reports />} />

          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;