import React, {useEffect, useState} from "react";
import {get} from "../../utils/utils";
import {Button, Card, CardGroup, Nav} from "react-bootstrap";
import receipt from '../../res/reciept.jpg';
import Select from "react-select";
import {changeStatus, fetchOrders} from "../../api/adminAPI";
import {Helmet} from "react-helmet";

function AdminOrders() {
    const [admin = {
        administrator: {},
        adminId: '',
        email: '',
        restaurant: {}
    }] = useState(get('admin-info'));
    const [restaurant = {
        restaurantId: null,
        name: '',
        location: '',
        locationZone: {
            name: ''
        },
        deliveryZones: [],
        menu: {},
        orders: []
    }] = useState(admin?.restaurant);
    const [orders = [{
        orderId: null,
        products: [],
        status: ''
    }], setOrders] = useState(restaurant?.orders);
    const options = [
        {value: 'DECLINED', label: 'Declined'},
        {value: 'ACCEPTED', label: 'Accepted'},
        {value: 'IN_DELIVERY', label: 'In delivery'},
        {value: 'DELIVERED', label: 'Delivered'}];
    const [changedOrder, setChangedOrder] = useState({
            orderId: null,
            status: ''
        }
    );
    const [error, setError] = useState('');

    useEffect(() => {
        fetchOrders(restaurant)
            .then(response => {
                setOrders(response.response)
            })
            .catch(error => {
                setError(error.response.data.message)
            })
    }, [])

    function handleSelect(selected, orderId) {
        setError('')
        setChangedOrder({
            orderId: orderId,
            status: selected.value
        })
    }

    function handleSubmit() {
        changeStatus(changedOrder)
            .then(() => {
                fetchOrders(restaurant)
                    .then(response => {
                        setOrders(response.response)
                    })
                    .catch(error => {
                        setError(error.response.data.message)
                    })
            })
            .catch(error => {
                setError(error.response.data.message)
            })
    }

    return (
        <div style={{backgroundColor: 'black', backgroundImage: 'url(' + receipt + ')', backgroundSize: 'cover'}}>
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
                            <h2>#{order.orderId} | Status: {order.status}</h2>
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
                            <Select
                                options={options}
                                onChange={(selected) => handleSelect(selected, order.orderId)}/>
                            <br/>
                            <Button
                                style={{width: 300}}
                                className={'btn btn-primary'}
                                onClick={handleSubmit}>
                                Change status
                            </Button>
                            {
                                changedOrder.orderId === order.orderId ? <h4 style={{color: 'red'}}>{error}</h4> : null
                            }
                        </Card>
                    </div>
                })
            }
        </div>
    );
}

export default AdminOrders;