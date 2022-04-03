import React, {useState} from "react";
import {sendLogin} from "../api/adminAPI";
import {useNavigate} from "react-router-dom";

function Login() {
    localStorage.clear();
    const navigate = useNavigate();
    const [login, setLogin] = useState({
        credential: '',
        password: ''
    });
    const [error, setError] = useState('');

    function handleChange(event) {
        const {name, value} = event.target
        setLogin(prevState => {
            return {
                ...prevState,
                [name]: value
            };
        })
    }

    function submit() {
        sendLogin(login)
            .then(adminData => {
                localStorage.setItem('admin-info', JSON.stringify(adminData))
                if(adminData.restaurant)
                    navigate("/adminHome");
                else navigate("/addRestaurant")
            })
            .catch(error => {
                setError(error.response.data.message)
            });
    }

    return (
        <div className="App">
            <header/>

            <h1>Login Page admin</h1>

            <div
                className='col-sm-6 offset-sm-3'>
                <input
                    name={'credential'}
                    type={'text'}
                    placeholder={'email'}
                    onChange={handleChange}
                />
                <br/>

                <input
                    name={'password'}
                    type={'password'}
                    placeholder={'password'}
                    onChange={handleChange}
                />
                <br/>

                <button
                    className={'btn btn-primary'}
                    onClick={submit}>
                    Login
                </button>

                <h2>
                    {error}
                </h2>

            </div>
        </div>
    );
}

export default Login;
