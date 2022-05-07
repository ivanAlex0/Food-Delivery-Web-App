import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import {Button, Card, Form, Nav} from "react-bootstrap";
import customerLogin from "../../res/customer_login.jpg";
import {Helmet} from "react-helmet";
import {loginToken} from "../../api/adminAPI";
import {sendLogin} from "../../api/customerAPI";

function CustomerLogin() {
    localStorage.clear();
    const navigate = useNavigate();
    const [accountDTO, setAccountDTO] = useState({
        credential: '',
        password: ''
    });
    const [error, setError] = useState('');

    function handleSubmit(event) {
        loginToken(accountDTO)
            .then(tokens => {
                localStorage.setItem("tokens", JSON.stringify(tokens))
                sendLogin(accountDTO)
                    .then(customerData => {
                        localStorage.setItem('customer-info', JSON.stringify(customerData))
                        navigate('/customer/home')
                    })
                    .catch(error => {
                        setError(error.response.data.message)
                    });
            })
            .catch(() => {
                setError("Invalid credentials")
            })
        event.preventDefault();
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
        <div style={{height: 800, backgroundImage: 'url(' + customerLogin + ')', backgroundSize: 'cover'}}>
            <Helmet>
                <title>🍔 Customer | Login</title>
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

            <Card style={{
                opacity: 0.85,
                left: 500,
                top: 130,
                width: 500,
                height: 475,
                backgroundColor: 'lightcoral',
                padding: 50
            }}>
                <Card.Title style={{justifyContent: 'center', display: 'flex', color: '#000', fontSize: 40}}>
                    Login as customer
                </Card.Title>
                <br/>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Email address</Form.Label>
                        <Form.Control
                            name={'credential'}
                            type={'email'}
                            placeholder={'Enter email...'}
                            onChange={handleChange}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Password</Form.Label>
                        <Form.Control
                            name={'password'}
                            type={'password'}
                            placeholder={'Enter password...'}
                            onChange={handleChange}/>
                    </Form.Group>

                    <text style={{color: 'red', justifyContent: 'center', display: 'flex'}}>
                        {error}
                    </text>

                    <Button variant="success" type="submit" style={{width: 400}}>
                        Login
                    </Button>
                </Form>

                <br/>
                <text style={{color: 'black'}}>
                    Don't have an account?
                </text>

                <Button style={{width: 100}} onClick={() => {
                    navigate('/customer/register')
                }}>
                    Register
                </Button>
            </Card>
        </div>
    );
}

export default CustomerLogin;
