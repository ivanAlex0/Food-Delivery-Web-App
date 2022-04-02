import React, {useState} from "react";
import {useNavigate,} from "react-router-dom";

function Login() {
    localStorage.clear();
    const [credential, setCredential] = useState("")
    const [password, setPassword] = useState("")
    const [errorMessage, setErrorMessage] = useState("")
    let navigate = useNavigate();

    async function login() {
        let item = {credential, password}
        let result = await fetch('http://localhost:8080/api/admin/auth',
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(item),
            }).catch((error) => {
            setErrorMessage(error)
        });

        result = await result.json();
        if (!result.message) {
            localStorage.setItem('admin-info', JSON.stringify(result))
            if (result.restaurant)
                navigate('/admin')
            else
                navigate('/addRestaurant')
        } else {
            setErrorMessage(result.message)
        }
    }

    return (
        <div className="App">
            <header/>

            <h1>Login Page admin</h1>

            <div className='col-sm-6 offset-sm-3'>
                <input type={'text'} placeholder={'email'} className={'form-control'}
                       onChange={(e) => setCredential(e.target.value)}/>
                <br/>

                <input type={'password'} placeholder={'password'} className={'form-control'}
                       onChange={(e) => setPassword(e.target.value)}/>
                <br/>

                <button onClick={login} className={'btn btn-primary'}>Login</button>

                <h1>
                    {errorMessage}
                </h1>

            </div>
        </div>
    );
}

export default Login;
