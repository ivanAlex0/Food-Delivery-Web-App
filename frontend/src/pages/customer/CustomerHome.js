import React, {useEffect, useState} from "react";
import {get} from "../../utils/utils";
import {useNavigate} from "react-router-dom";
import {fetchRestaurants} from "../../api/customerAPI";
import Select from "react-select";

function CustomerHome() {
    const [customer = {
        customerId: null,
        email: '',
        name: '',
        address: '',
        addressZone: {
            id: null
        },
    }, setCustomer] = useState(get('customer-info'));
    const [restaurants, setRestaurants] = useState([{
        restaurantId: null,
        name: '',
        deliveryZones: [],
        location: '',
        locationZone: {},
        menu: [],
    }]);
    const [currentRestaurant, setCurrentRestaurant] = useState(restaurants[0]);
    const navigate = useNavigate();

    useEffect(() => {
        if (!customer)
            navigate('/customer/login');

        fetchRestaurants()
            .then(response => {
                setRestaurants(response.response)
            })
            .catch(error => {
                console.warn(error)
            });
    }, [])

    function handleSelect(selected) {
        setCurrentRestaurant(selected.value)
    }

    return (
        <div>
            Welcome, user {customer?.email}

            <br/>
            <br/>
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
            <ul>
                {
                    currentRestaurant.menu.categories?.map(cat => {
                        return <li key={cat.categoryId}>
                            {cat.category}
                            <ul>
                                {
                                    cat.foodList.map(food => {
                                        return <li key={food.foodId}>
                                            {food.name} ---- {food.description} ---- {food.price}
                                        </li>
                                    })
                                }
                            </ul>
                        </li>
                    })
                }
            </ul>
        </div>
    );
}

export default CustomerHome;