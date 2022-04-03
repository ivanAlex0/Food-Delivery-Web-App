import "./App.css"
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Register from "./pages/Register";
import Login from "./pages/Login";
import Menu from "./pages/Menu";
import ErrorPage from "./pages/ErrorPage";
import AddRestaurant from "./pages/AddRestaurant";
import AdminHome from "./pages/AdminHome";

function App() {
    return (
        <Router>
            <Routes>
                <Route path={'/'} element={<Login/>}/>
                <Route path={'/login'} element={<Login/>}/>
                <Route path={'/register'} element={<Register/>}/>
                <Route path={'/menu'} element={<Menu/>}/>
                <Route path={'/addRestaurant'} element={<AddRestaurant/>}/>
                <Route path={'/adminHome'} element={<AdminHome/>}/>
                <Route path={'*'} element={<ErrorPage/>}/>
            </Routes>
        </Router>
    );
}

export default App;