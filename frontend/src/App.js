import "./App.css"
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import Register from "./pages/Register";
import Login from "./pages/Login";
import Admin from "./pages/Admin";
import ErrorPage from "./pages/ErrorPage";
import AddRestaurant from "./pages/AddRestaurant";

function App() {
    return (
        <Router>
            <Routes>
                <Route path={'/'} element={<Login/>}/>
                <Route path={'/login'} element={<Login/>}/>
                <Route path={'/register'} element={<Register/>}/>
                <Route path={'/admin'} element={<Admin/>}/>
                <Route path={'/addRestaurant'} element={<AddRestaurant/>}/>
                <Route path={'*'} element={<ErrorPage/>}/>
            </Routes>
        </Router>
    );
}

export default App;