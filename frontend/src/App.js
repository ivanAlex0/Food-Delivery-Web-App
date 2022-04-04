import "./App.css"
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import AdminRegister from "./pages/admin/AdminRegister";
import AdminLogin from "./pages/admin/AdminLogin";
import AdminMenu from "./pages/admin/AdminMenu";
import ErrorPage from "./pages/ErrorPage";
import AddRestaurant from "./pages/admin/AddRestaurant";
import AdminHome from "./pages/admin/AdminHome";
import CustomerRegister from "./pages/customer/CustomerRegister";
import CustomerHome from "./pages/customer/CustomerHome";
import CustomerLogin from "./pages/customer/CustomerLogin";

function App() {
    return (
        <Router>
            <Routes>
                <Route path={'/'} element={<AdminLogin/>}/>
                <Route path={'/admin/login'} element={<AdminLogin/>}/>
                <Route path={'/admin/register'} element={<AdminRegister/>}/>
                <Route path={'/admin/menu'} element={<AdminMenu/>}/>
                <Route path={'/admin/addRestaurant'} element={<AddRestaurant/>}/>
                <Route path={'/admin/home'} element={<AdminHome/>}/>
                <Route path={'/customer/login'} element={<CustomerLogin/>}/>
                <Route path={'/customer/register'} element={<CustomerRegister/>}/>
                <Route path={'/customer/home'} element={<CustomerHome/>}/>
                <Route path={'*'} element={<ErrorPage/>}/>
            </Routes>
        </Router>
    );
}

export default App;