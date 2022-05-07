import React, {useEffect, useState} from "react";
import {get} from "../../utils/utils";
import {useNavigate} from "react-router-dom";
import {fetchOrders} from "../../api/customerAPI";
import {Card, CardGroup, Nav} from "react-bootstrap";
import reciept from "../../res/reciept3.jpg";
import {Helmet} from "react-helmet";
import {refreshToken} from "../../api/adminAPI";

function CustomerOrders() {
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
    const [orders, setOrders] = useState([{
        orderId: null,
        products: [{
            item: {
                name: '',
                price: null,
            },
            quantity: null
        }],
        state: {
            orderStatus: ''
        },
        restaurantName: ''
    }]);
    const navigate = useNavigate();

    useEffect(() => {
        if (!customer)
            navigate('/customer/login')

        fetchOrders(customer.customerId, tokens.accessToken)
            .then(response => {
                setOrders(response.response)
            })
            .catch(error => {
                if (error.response.status === 403) {
                    refreshToken(tokens.refreshToken)
                        .then(tokens => {
                            setTokens(tokens)
                            localStorage.setItem("tokens", JSON.stringify(tokens))
                            fetchOrders(customer.customerId, tokens.accessToken)
                                .then(response => {
                                    setOrders(response.response)
                                })
                                .catch(error => {
                                    console.warn(error.response.data.message)
                                })
                        })
                } else
                    console.warn(error.response.data.message)
            })
    }, [])

    return (
        <div style={{backgroundColor: 'black', backgroundImage: 'url(' + reciept + ')', backgroundSize: 'cover'}}>
            <Helmet>
                <title>🍔 Customer | Orders</title>
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
            </Nav>

            <br/>
            <br/>
            <br/>
            <br/>

            {
                orders.map((order) => {
                    return <div style={{padding: 100}}>
                        <Card key={order.products + order.orderId}
                              style={{
                                  backgroundColor: 'white',
                                  padding: 20,
                                  border: '5px solid',
                                  borderColor: 'cadetblue',
                                  borderRadius: 20
                              }}>
                            <h2>#{order.orderId} | Status: {order.state.orderStatus}</h2>
                            <h2>{order.restaurantName}</h2>
                            <CardGroup>
                                {
                                    order.products.map(product => {
                                        return <div style={{height: 100, width: 250, padding: 5}}>
                                            <Card key={product.item + product.quantity}
                                                  style={{
                                                      padding: 10,
                                                      borderRadius: 20,
                                                      backgroundColor: 'slategray',
                                                      color: 'white',
                                                      border: '2px solid',
                                                      borderColor: 'white',
                                                      height: '100%'
                                                  }}>
                                                <Card.Title style={{display: 'flex', justifyContent: 'center'}}>
                                                    {product.item.name} * {product.quantity}
                                                </Card.Title>
                                                <Card.Text style={{display: 'flex', justifyContent: 'center'}}>
                                                    Price: {product.quantity * product.item.price}
                                                </Card.Text>
                                            </Card>
                                        </div>
                                    })
                                }
                            </CardGroup>
                            <br/>
                        </Card>
                    </div>
                })
            }
        </div>
    );
}

export default CustomerOrders;