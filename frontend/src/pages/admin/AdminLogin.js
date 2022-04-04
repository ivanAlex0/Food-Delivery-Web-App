import React, {useState} from "react";
import {sendLogin} from "../../api/adminAPI";
import {useNavigate} from "react-router-dom";

function AdminLogin() {
    localStorage.clear();
    const navigate = useNavigate();
    const [accountDTO, setAccountDTO] = useState({
        credential: '',
        password: ''
    });
    const [error, setError] = useState('');

    function submit() {
        sendLogin(accountDTO)
            .then(adminData => {
                localStorage.setItem('admin-info', JSON.stringify(adminData))
                if(adminData.restaurant)
                    navigate("/admin/home");
                else navigate("/admin/addRestaurant")
            })
            .catch(error => {
                setError(error.response.data.message)
            });
    }

    function handleChange(event) {
        const {name, value} = event.target
        setAccountDTO(prevState => {
            return {
                ...prevState,
                [name]: value
            };
        })
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
                    className={'form-control'}
                    onChange={handleChange}
                />
                <br/>

                <input
                    name={'password'}
                    type={'password'}
                    placeholder={'password'}
                    className={'form-control'}
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

export default AdminLogin;
