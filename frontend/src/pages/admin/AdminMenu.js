import React, {useEffect, useState} from "react";
import {Card, Nav} from "react-bootstrap";
import {foodList, get} from "../../utils/utils";
import {useNavigate} from "react-router-dom";
import background from '../../res/restaurant.jpg';
import {Helmet} from "react-helmet";
import {generatePDF, refreshToken} from "../../api/adminAPI";

function AdminMenu() {
    const [tokens, setTokens] = useState(get('tokens'))
    const [admin] = useState(get('admin-info'));
    const [restaurant] = useState(admin?.restaurant);
    const navigate = useNavigate();


    useEffect(() => {
        if (!admin)
            navigate('/admin/login')
    }, [])

    function generatePDFFunction(){
        generatePDF(admin, tokens.accessToken)
            .then()
            .catch(error => {
                if(error.response.status === 403){
                    refreshToken(tokens.refreshToken)
                        .then(tokens => {
                            setTokens(tokens)
                            localStorage.setItem('tokens', JSON.stringify(tokens))
                            generatePDF(admin, tokens.accessToken)
                                .then()
                        })
                }
            })
    }

    return (
        <div>
            <Helmet>
                <title>🍕 Admin | Menu</title>
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

            <div style={{padding: 100, backgroundImage: 'url(' + background + ')', backgroundSize: 'contain'}}>

                <button style={{backgroundColor: 'yellow'}}
                onClick={() => generatePDFFunction()}>
                    Generate PDF
                </button>

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
                        {restaurant?.name}'s Menu
                    </Card.Title>
                </Card>
                {restaurant?.menu.categories?.map(category => {
                    return <div>
                        <h1 style={{color: 'white', fontSize: 75}}>
                            {category.category}
                        </h1>
                        <Card style={{
                            border: '15px solid',
                            borderColor: 'white',
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
                                        return <div style={{height: 150, padding: 20}}>
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

export default AdminMenu;