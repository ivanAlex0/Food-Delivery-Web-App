import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import Select from "react-select";
import {addFood, fetchMenu, refreshToken} from "../../api/adminAPI";
import {get} from "../../utils/utils";
import {Button, Card, Form, Nav} from "react-bootstrap";
import addFoodBck from '../../res/addfood.jpg';
import {Helmet} from "react-helmet";


function AddFood() {
    const [tokens, setTokens] = useState(get("tokens"));
    const navigate = useNavigate();
    const [admin = {
        administrator: {},
        adminId: '',
        email: '',
        restaurant: {}
    }] = useState(get('admin-info'));
    const [restaurant = {
        name: '',
        location: '',
        locationZone: {
            name: ''
        },
        deliveryZones: [],
        menu: {}
    }] = useState(admin?.restaurant);
    const [menu = {
        menuId: '',
        categories: []
    }] = useState(restaurant?.menu);
    const [categories = [{
        categoryId: '',
        category: '',
        foodList: [{
            foodId: '',
            name: '',
            description: '',
            price: null
        }]
    }]] = useState(menu?.categories);
    const [food, setFood] = useState({
        name: '',
        description: '',
        price: null
    });
    const [category, setCategory] = useState({
        categoryId: '',
        category: ''
    });
    const [error, setError] = useState('');

    function handleChange(event) {
        const {name, value} = event.target;
        setFood(prevState => {
            return {
                ...prevState,
                [name]: value
            };
        })
    }

    function handleSubmit(event) {
        addFood(category, food, tokens.accessToken)
            .then(() => {
                fetchMenu(restaurant, tokens.accessToken)
                    .then(response => {
                        let newAdmin = {
                            ...admin,
                            restaurant: {
                                ...restaurant,
                                menu: response
                            }
                        }
                        localStorage.setItem('admin-info', JSON.stringify(newAdmin));
                        window.location.reload(false);
                    })
                    .catch(error => {
                        setError(error.response.data.message)
                    })
            })
            .catch(error => {
                if (error.response.status === 403) {
                    refreshToken(tokens.refreshToken)
                        .then(tokens => {
                            console.warn(tokens)
                            setTokens(tokens)
                            localStorage.setItem("tokens", JSON.stringify(tokens))
                            addFood(category, food, tokens.accessToken)
                                .then(() => {
                                    fetchMenu(restaurant, tokens.accessToken)
                                        .then(response => {
                                            let newAdmin = {
                                                ...admin,
                                                restaurant: {
                                                    ...restaurant,
                                                    menu: response
                                                }
                                            }
                                            localStorage.setItem('admin-info', JSON.stringify(newAdmin));
                                            window.location.reload(false);
                                        })
                                        .catch(error => {
                                            setError(error.response.data.message)
                                        })
                                })
                                .catch(error => {
                                    setError(error.response.data.message)
                                })
                        })
                } else {
                    setError(error.response.data.message)
                }
            });
        event.preventDefault();
    }

    useEffect(() => {
        if (!admin)
            navigate("/admin/login");
    }, [])


    return (
        <div style={{height: 800, backgroundImage: 'url(' + addFoodBck + ')', backgroundSize: 'cover'}}>
            <Helmet>
                <title>🍕 Admin | Add Food</title>
            </Helmet>

            <Nav
                style={{
                    backgroundColor: 'black', height: 55, overflow: 'hidden',
                    position: 'fixed',
                    top: 0,
                    zIndex: 100,
                    width: '100%'
                }}>
                <Nav.Item style={{paddingTop: 7, paddingLeft: 10}}>
                    <Nav.Link href="/admin/login" style={{color: 'white', fontSize: 20}}>Home</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{paddingTop: 7, paddingLeft: 300}}>
                    <Nav.Link href="/admin/addFood" style={{color: 'white', fontSize: 20}}>Add Food</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{paddingTop: 7, paddingLeft: 200}}>
                    <Nav.Link href="/admin/menu" style={{color: 'white', fontSize: 20}}>Menu</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{paddingTop: 7, paddingLeft: 200}}>
                    <Nav.Link href="/admin/orders" style={{color: 'white', fontSize: 20}}>Orders</Nav.Link>
                </Nav.Item>
            </Nav>

            <br/>
            <br/>
            <Card style={{
                opacity: 0.85,
                left: 500,
                top: 50,
                width: 500,
                height: 600,
                backgroundColor: 'orange',
                padding: 50,
                borderRadius: 38
            }}>
                <Card.Title style={{justifyContent: 'center', display: 'flex', color: '#000', fontSize: 40}}>
                    Add a new food
                </Card.Title>
                <br/>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Category</Form.Label>
                        <Select options={
                            categories.map(cat => {
                                return {
                                    value: cat,
                                    label: cat.category
                                }
                            })
                        }
                                onChange={(selected) => setCategory(selected.value)}>
                        </Select>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Name</Form.Label>
                        <Form.Control
                            name={'name'}
                            type={'text'}
                            placeholder={'Enter name...'}
                            onChange={handleChange}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Description</Form.Label>
                        <Form.Control
                            name={'description'}
                            type={'text'}
                            placeholder={'Enter description...'}
                            onChange={handleChange}/>
                    </Form.Group>

                    <Form.Group className={'mb-3'}>
                        <Form.Label style={{justifyContent: 'center', display: 'flex'}}>Price</Form.Label>
                        <Form.Control
                            name={'price'}
                            type={'number'}
                            step="0.01"
                            placeholder={'Enter price...'}
                            onChange={handleChange}/>
                    </Form.Group>

                    <text style={{color: 'red', justifyContent: 'center', display: 'flex'}}>
                        {error}
                    </text>

                    <Button variant="primary" type="submit" style={{width: 400}}>
                        Add food
                    </Button>
                </Form>
            </Card>
        </div>
    );
}

export default AddFood;