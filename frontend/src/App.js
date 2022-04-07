import "./App.css"
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import AdminRegister from "./pages/admin/AdminRegister";
import AdminLogin from "./pages/admin/AdminLogin";
import AddFood from "./pages/admin/AddFood";
import ErrorPage from "./pages/ErrorPage";
import AddRestaurant from "./pages/admin/AddRestaurant";
import CustomerRegister from "./pages/customer/CustomerRegister";
import CustomerHome from "./pages/customer/CustomerHome";
import CustomerLogin from "./pages/customer/CustomerLogin";
import CustomerOrders from "./pages/customer/CustomerOrders";
import AdminOrders from "./pages/admin/AdminOrders";
import AdminMenu from "./pages/admin/AdminMenu";
import CustomerCart from "./pages/customer/CustomerCart";

function App() {
    return (
        <Router>
            <Routes>
                <Route path={'/'} element={<CustomerLogin/>}/>
                <Route path={'/admin/login'} element={<AdminLogin/>}/>
                <Route path={'/admin/register'} element={<AdminRegister/>}/>
                <Route path={'/admin/addFood'} element={<AddFood/>}/>
                <Route path={'/admin/addRestaurant'} element={<AddRestaurant/>}/>
                <Route path={'admin/orders'} element={<AdminOrders/>}/>
                <Route path={'admin/menu'} element={<AdminMenu/>}/>
                <Route path={'/customer/login'} element={<CustomerLogin/>}/>
                <Route path={'/customer/register'} element={<CustomerRegister/>}/>
                <Route path={'/customer/home'} element={<CustomerHome/>}/>
                <Route path={'/customer/orders'} element={<CustomerOrders/>}/>
                <Route path={'/customer/cart'} element={<CustomerCart/>}/>
                <Route path={'*'} element={<ErrorPage/>}/>
            </Routes>
        </Router>
    );
}

export default App;