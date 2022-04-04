import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import {sendRegister} from "../../api/adminAPI";

function AdminRegister() {
    localStorage.clear();
    const [accountDTO, setAccountDTO] = useState({
        credential: '',
        password: ''
    });
    const [error, setError] = useState("");
    const navigate = useNavigate();

    function submit() {
        console.log(JSON.stringify(accountDTO))
        sendRegister(accountDTO)
            .then(response => {
                localStorage.setItem('admin-info', JSON.stringify(response));
                navigate('/admin/addRestaurant');
            })
            .catch(error => {
                console.warn(error.response)
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

            <h1>Register admin</h1>

            <div
                className='col-sm-6 offset-sm-3'>
                <input
                    name={'credential'}
                    type={'text'}
                    placeholder={'email'}
                    className={'form-control'}
                    onChange={handleChange}/>
                <br/>

                <input
                    name={'password'}
                    type={'password'}
                    placeholder={'password'}
                    className={'form-control'}
                    onChange={handleChange}/>
                <br/>

                <button
                    onClick={submit}
                    className={'btn btn-primary'}>
                    Register
                </button>

                <h1>
                    {error}
                </h1>

            </div>
        </div>
    );
}

export default AdminRegister;