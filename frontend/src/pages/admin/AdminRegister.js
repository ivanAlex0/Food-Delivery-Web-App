import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import {sendRegister} from "../../api/adminAPI";
import {Button, Card, Form, Nav} from "react-bootstrap";
import adminRegister from "../../res/admin_register.jpg";
import {Helmet} from "react-helmet";

function AdminRegister() {
    localStorage.clear();
    const [accountDTO, setAccountDTO] = useState({
        credential: '',
        password: ''
    });
    const [error, setError] = useState("");
    const navigate = useNavigate();

    function handleSubmit(event) {
        sendRegister(accountDTO)
            .then(response => {
                localStorage.setItem('admin-info', JSON.stringify(response));
                navigate('/admin/login');
            })
            .catch(error => {
                setError(error.response.data.message)
            });
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
        <div style={{height: 1000, backgroundImage: 'url(' + adminRegister + ')', backgroundSize: 'cover'}}>
            <Helmet>
                <title>🍕 Admin | Orders</title>
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
                height: 500,
                backgroundColor: 'seagreen',
                padding: 50
            }}>
                <Card.Title style={{justifyContent: 'center', display: 'flex', color: '#000', fontSize: 40}}>
                    Register as admin
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

                    <Button variant="warning" type="submit" style={{width: 400}}>
                        Register
                    </Button>
                </Form>

                <br/>
                <text style={{color: 'black'}}>
                    Already have an account?
                </text>

                <Button variant={'secondary'} style={{width: 100}} onClick={() => {
                    navigate('/admin/login')
                }}>
                    Login
                </Button>
            </Card>
        </div>
    );
}

export default AdminRegister;