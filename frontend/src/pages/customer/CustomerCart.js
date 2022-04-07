import React, {useEffect, useState} from "react";
import {get} from "../../utils/utils";
import {sendOrder} from "../../api/customerAPI";
import {Button, Card, Nav} from "react-bootstrap";
import {Helmet} from "react-helmet";
import cart from "../../res/cart.jpg";

function CustomerCart() {
    const [customer = {
        customerId: null,
        email: '',
        name: '',
        address: '',
        addressZone: {
            id: null
        },
    }] = useState(get('customer-info'));
    const [products, setProducts] = useState(get('cart-products'));
    const [currentRestaurant] = useState(get('current-restaurant'));
    const [order, setOrder] = useState({
        products: []
    });
    const [error, setError] = useState('');

    useEffect(() => {
        setOrder({
            products: products
        });
    }, [])

    function handleAddQuantity(item, value) {
        const index = products.findIndex(function (element) {
            return element.item.name === item.name
        });

        products[index].quantity += value;

        if (products[index].quantity === 0) {
            products.splice(index, 1);
            console.log(products)
        }

        setProducts(products)
        setOrder({
            products: products
        });
    }

    function handleSubmit() {
        sendOrder(order, customer.customerId, currentRestaurant.restaurantId)
            .then(() => {
                setError('Your order has successfully been sent to the ' + currentRestaurant.name + ' restaurant');
            })
            .catch(error => {
                setError(error.response.data.message)
            })
        setProducts([])
        localStorage.setItem('current-restaurant', null)
        localStorage.setItem('cart-products', null)
    }

    return (
        <div style={{height: 800, backgroundImage: 'url(' + cart + ')', backgroundSize: 'cover'}}>
            <Helmet>
                <title>🛒 Customer | Add Restaurant</title>
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
                    <Nav.Link href="/customer/home" style={{color: 'white', fontSize: 20}} onClick={() => {
                        localStorage.setItem('cart-products', JSON.stringify(products));
                    }}>Menu</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{paddingTop: 7, paddingLeft: 300}}>
                    <Nav.Link href="/customer/orders" style={{color: 'white', fontSize: 20}}>Orders</Nav.Link>
                </Nav.Item>
                <Nav.Item style={{paddingTop: 7, paddingLeft: 465}}>
                    <Nav.Link href="/customer/cart" style={{color: 'white', fontSize: 20}}>Cart</Nav.Link>
                </Nav.Item>
            </Nav>

            <br/>
            <br/>
            <br/>

            {
                products?.map(product => {
                    return <div style={{height: 220, padding: 20}}>
                        <Card style={{
                            borderRadius: 25,
                            backgroundColor: 'slategray',
                            color: 'white',
                            border: '2px solid',
                            borderColor: 'red',
                            height: '100%'
                        }}>
                            <Card.Body>
                                <Card.Title>
                                    {product.item.name}
                                </Card.Title>
                                <Card.Text>
                                    {product.item.description}
                                    <br/>
                                    Price: {product.item.price}
                                </Card.Text>
                                <Button style={{width: 45}}
                                        onClick={() => handleAddQuantity(product.item, -1)}>
                                    -
                                </Button>
                                {product.quantity}
                                <Button style={{width: 45}}
                                        onClick={() => handleAddQuantity(product.item, +1)}>
                                    +
                                </Button>
                            </Card.Body>
                        </Card>
                    </div>
                })
            }

            <h2>
                Total: {
                products?.map((product) => product.item.price * product.quantity).reduce((acc, amount) => acc + amount, 0)
            }
            </h2>

            <button style={{width: 500, backgroundColor: 'indianred'}}
                    className={'btn btn-primary'}
                    onClick={handleSubmit}>
                Place Order
            </button>

            <h2>{error}</h2>
        </div>
    );
}

export default CustomerCart;