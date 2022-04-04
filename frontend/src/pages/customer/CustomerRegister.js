import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {sendRegister} from "../../api/customerAPI";
import {fetchZones} from "../../api/adminAPI";
import Select from "react-select";

function CustomerRegister() {
    localStorage.clear();
    const [customer, setCustomer] = useState({
        email: '',
        password: '',
        name: '',
        address: '',
        addressZone: {
            id: null
        }
    });
    const [zones, setZones] = useState([]);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    useEffect(() => {

        fetchZones()
            .then(response => {
                setZones(response)
            })
            .catch(error => {
                setError(error.response.data.message)
            });
    }, [])

    function handleChange(event) {
        const {name, value} = event.target
        setCustomer(prevState => {
            return {
                ...prevState,
                [name]: value
            };
        })
    }

    function handleSelect(selected) {
        setCustomer(prevState => {
            return {
                ...prevState,
                'addressZone': selected.value
            };
        })
    }

    function submit() {
        console.log(customer)
        sendRegister(customer)
            .then(response => {
                localStorage.setItem('customer-info', JSON.stringify(response));
                navigate('/customer/home');
            })
            .catch(error => {
                setError(error.response.data.message)
            });
    }

    return (
        <div className="App">
            <header/>

            <h1>Register customer</h1>

            <br/>
            <div
                className='col-sm-6 offset-sm-3'>
                <input
                    name={'email'}
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

                <input
                    name={'name'}
                    type={'text'}
                    placeholder={'name'}
                    className={'form-control'}
                    onChange={handleChange}/>
                <br/>

                <input
                    name={'address'}
                    type={'text'}
                    placeholder={'address'}
                    className={'form-control'}
                    onChange={handleChange}/>
                <br/>

                <Select
                    options={
                        zones.map((zone) => {
                            return {
                                value: {
                                    id: zone.id
                                },
                                label: zone.name
                            }
                        })
                    }
                    onChange={handleSelect}
                >
                </Select>

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

export default CustomerRegister;