import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {sendRegister} from "../../api/customerAPI";
import {fetchZones, loginToken} from "../../api/adminAPI";
import Select from "react-select";
import {Button, Card, Form, Nav} from "react-bootstrap";
import customerRegisterImg from "../../res/customer_register.jpg";
import {Helmet} from "react-helmet";

function CustomerRegister() {
    localStorage.clear();
    const [customer, setCustomer] = useState({
        name: '',
        address: '',
        addressZone: {
            id: null
        }
    });
    const [user, setUser] = useState({
        email: '',
        password: '',
    });
    const [customerRegister, setCustomerRegister] = useState({
        customer: {
            name: '',
            address: '',
            addressZone: {
                id: null
            }
        },
        user: {
            email: '',
            password: '',
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

    function handleChangeUser(event) {
        const {name, value} = event.target
        setUser(prevState => {
            return {
                ...prevState,
                [name]: value
            };
        })
    }

    function handleChangeCustomer(event) {
        const {name, value} = event.target
        setCustomer(prevState => {
            return {
                ...prevState,
                [name]: value
            };
        })
        setCustomerRegister(
            {
                customer: customer,
                user: user
            }
        )
    }

    async function handleSelect(selected) {
        setCustomerRegister(
            {
                customer: {
                    ...customer,
                    'addressZone': selected.value
                },
                user: user
            }
        )
    }

    function handleSubmit(event) {
        sendRegister(customerRegister)
            .then(response => {
                localStorage.setItem('customer-info', JSON.stringify(response));
                navigate('/customer/login');
            })
            .catch(error => {
                setError(error.response.data.message)
            });
        event.preventDefault()
    }

    return (
        <div style={{height: 850, backgroundImage: 'url(' + customerRegisterImg + ')', backgroundSize: 'cover'}}>
            <Helmet>
                <title>🍔 Customer | Register</title>
            </Helmet>

            <Nav
                style={{
                    backgroundColor: 'black', height: 55, overflow: 'hidden',
                    position: 'fixed',
                    top: 0,
                    zIndex: 100,
                    width: '100%'
                }}>
                <Nav.Item style={{paddingTop: 7, paddingLeft: 300}}>
                    <Nav.Link href="/admin/login" style={{color: 'white', fontSize: 20}}>Admin</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{paddingTop: 7, paddingLeft: 700}}>
                    <Nav.Link href="/customer/login" style={{color: 'white', fontSize: 20}}>Customer</Nav.Link>
                </Nav.Item>
            </Nav>

            <br/>
            <br/>
            <br/>

            <Card style={{
                opacity: 0.85,
                left: 500,
                top: 5,
                width: 500,
                height: 750,
                backgroundColor: 'darkcyan',
                padding: 50
            }}>
                <Card.Title style={{justifyContent: 'center', display: 'flex', color: '#000', fontSize: 40}}>
                    Register as customer
                </Card.Title>
                <br/>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Email address</Form.Label>
                        <Form.Control
                            name={'email'}
                            type={'email'}
                            placeholder={'Enter email...'}
                            onChange={handleChangeUser}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Password</Form.Label>
                        <Form.Control
                            name={'password'}
                            type={'password'}
                            placeholder={'Enter password...'}
                            onChange={handleChangeUser}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Name</Form.Label>
                        <Form.Control
                            name={'name'}
                            type={'text'}
                            placeholder={'Enter name...'}
                            onChange={handleChangeCustomer}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Address</Form.Label>
                        <Form.Control
                            name={'address'}
                            type={'text'}
                            placeholder={'Enter address...'}
                            onChange={handleChangeCustomer}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Zone</Form.Label>
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
                            onChange={handleSelect}>
                        </Select>
                    </Form.Group>


                    <text style={{color: 'red', justifyContent: 'center', display: 'flex'}}>
                        {error}
                    </text>

                    <Button variant="warning" type="submit" style={{width: 400}}>
                        Register
                    </Button>
                </Form>

                <br/>
                <text style={{color: 'black'}}>
                    Already have an account?
                </text>

                <Button variant={'secondary'} style={{width: 100}} onClick={() => {
                    navigate('/customer/login')
                }}>
                    Login
                </Button>
            </Card>
        </div>
    );
}

export default CustomerRegister;