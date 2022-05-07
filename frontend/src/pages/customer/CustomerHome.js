import React, {useEffect, useState} from "react";
import {foodList, get} from "../../utils/utils";
import {useNavigate} from "react-router-dom";
import {fetchRestaurants} from "../../api/customerAPI";
import Select from "react-select";
import {Button, Card, Nav} from "react-bootstrap";
import background from "../../res/restaurant.jpg";
import {Helmet} from "react-helmet";
import {refreshToken} from "../../api/adminAPI";

function CustomerHome() {
    const [tokens, setTokens] = useState(get("tokens"))
    const [customer = {
        customerId: null,
        email: '',
        name: '',
        address: '',
        addressZone: {
            id: null
        },
    }] = useState(get('customer-info'));
    const [restaurants, setRestaurants] = useState([{
        restaurantId: null,
        name: '',
        deliveryZones: [],
        location: '',
        locationZone: {},
        menu: [],
    }]);
    const [products, setProducts] = useState(get('cart-products'));
    const [currentRestaurant, setCurrentRestaurant] = useState(get('current-restaurant'));
    const navigate = useNavigate();

    useEffect(() => {
        if (!customer)
            navigate('/customer/login');
        if (products && products.length > 0) {
            for (let i = 0; i < products.length; i++) {
                document.getElementById(products[i].item.name).disabled = true;
            }
        } else {
            let elems = document.body.getElementsByTagName("<Button>");

            for (let i = 0; i < elems.length; i++) {
                elems[i].disabled = false;
            }
        }

        fetchRestaurants(tokens.accessToken)
            .then(response => {
                setRestaurants(response.response)
            })
            .catch(error => {
                if (error.response.status === 403) {
                    refreshToken(tokens.refreshToken)
                        .then(tokens => {
                            setTokens(tokens)
                            localStorage.setItem("tokens", JSON.stringify(tokens))
                            fetchRestaurants(tokens.accessToken)
                                .then(response => {
                                    setRestaurants(response.response)
                                })
                                .catch(error => {
                                    console.warn(error)
                                })
                        })
                } else
                    console.warn(error)
            });
    }, [])

    function handleSelect(selected) {
        setProducts([])
        localStorage.setItem('cart-products', null)
        setCurrentRestaurant(selected.value)

        let elems = document.body.getElementsByTagName("button");

        for (let i = 0; i < elems.length; i++) {
            elems[i].disabled = false;
        }
    }

    function handleAddToCart(item) {
        if (products.some(product => product.item.foodId === item.foodId)) {
            console.warn("Added already")
        } else {
            setProducts(prevState => {
                return [
                    ...prevState,
                    {
                        item: item,
                        quantity: 1
                    }
                ]
            })
            document.getElementById(item.name).disabled = true;
        }
    }


    return (
        <div style={{backgroundColor: 'gray', padding: 0}}>
            <Helmet>
                <title>🍔 Customer | Home</title>
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
                    <Nav.Link href="/customer/login" style={{color: 'white', fontSize: 20}}>Home</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{paddingTop: 7, paddingLeft: 400}}>
                    <Nav.Link href="/customer/home" style={{color: 'white', fontSize: 20}}>Menu</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{paddingTop: 7, paddingLeft: 300}}>
                    <Nav.Link href="/customer/orders" style={{color: 'white', fontSize: 20}}>Orders</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{paddingTop: 7, paddingLeft: 465}}>
                    <Nav.Link onClick={() => {
                        localStorage.setItem('cart-products', JSON.stringify(products));
                        localStorage.setItem('current-restaurant', JSON.stringify(currentRestaurant));
                    }} href="/customer/cart" style={{color: 'white', fontSize: 20}}>Cart</Nav.Link>
                </Nav.Item>
            </Nav>

            <br/>
            <br/>
            <br/>
            <h1>Select a restaurant</h1>
            <Select
                options={restaurants.map(restaurant => {
                    return {
                        value: restaurant,
                        label: restaurant.name
                    }
                })}
                onChange={handleSelect}>
            </Select>

            <br/>
            <br/>

            <div style={{
                borderRadius: 30,
                border: '10px solid',
                padding: 100,
                backgroundImage: 'url(' + background + ')',
                backgroundSize: 'contain'
            }}>
                <Card style={{
                    borderRadius: 45,
                    backgroundColor: 'yellowgreen',
                    display: 'flex',
                    justifyContent: 'center',
                    width: 400,
                    left: 450,
                    height: 150
                }}>
                    <Card.Title style={{fontSize: 45, display: 'flex', justifyContent: 'center'}}>
                        {currentRestaurant?.name}'s Menu
                    </Card.Title>
                </Card>
                {currentRestaurant?.menu.categories?.map(category => {
                    return <div>
                        <h1 style={{color: 'white', fontSize: 75}}>
                            {category.category}
                        </h1>
                        <Card style={{
                            border: '20px solid',
                            borderColor: 'lightslategray',
                            borderRadius: 30,
                            backgroundImage: 'url(' + foodList[category.categoryId % 6] + ')',
                            backgroundSize: 'cover'
                        }}>
                            <Card.Body>
                                <Card.Title style={{color: 'white'}}>
                                    {category.category}
                                </Card.Title>
                                {
                                    category.foodList.map(food => {
                                        return <div style={{height: 220, padding: 20}}>
                                            <Card style={{
                                                backgroundColor: 'slategray',
                                                color: 'white',
                                                border: '2px solid',
                                                borderColor: 'white',
                                                height: '100%'
                                            }}>
                                                <Card.Body>
                                                    <Card.Title>
                                                        {food.name}
                                                    </Card.Title>
                                                    <Card.Text>
                                                        {food.description}
                                                        <br/>
                                                        Price: {food.price}
                                                    </Card.Text>
                                                    <Button
                                                        id={food.name}
                                                        className={'btn btn-primary'}
                                                        onClick={() => handleAddToCart(food)}>
                                                        Add to cart
                                                    </Button>
                                                </Card.Body>
                                            </Card>
                                        </div>
                                    })
                                }
                            </Card.Body>
                        </Card>
                        <br/>
                        <br/>
                        <br/>
                    </div>
                })}
            </div>
        </div>
    );
}

export default CustomerHome;