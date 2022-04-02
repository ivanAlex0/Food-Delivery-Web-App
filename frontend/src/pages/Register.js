import React, {useState} from "react";
import {useNavigate} from "react-router-dom";

function Register() {
    localStorage.clear();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const navigate = useNavigate();

    async function register() {
        let item = {email, password}
        let result = await fetch('http://localhost:8080/api/admin/register',
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
            navigate('/addRestaurant')
        } else {
            setErrorMessage(result.message)
        }
    }

    return (
        <div className="App">
            <header/>

            <h1>Register admin</h1>

            <div className='col-sm-6 offset-sm-3'>
                <input type={'text'} placeholder={'email'} className={'form-control'}
                       onChange={(e) => setEmail(e.target.value)}/>
                <br/>

                <input type={'password'} placeholder={'password'} className={'form-control'}
                       onChange={(e) => setPassword(e.target.value)}/>
                <br/>

                <button onClick={register} className={'btn btn-primary'}>Login</button>

                <h1>
                    {errorMessage}
                </h1>

            </div>
        </div>
    );
}

export default Register;