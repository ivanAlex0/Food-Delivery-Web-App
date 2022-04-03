import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import Select from "react-select";
import {addFood, fetchMenu} from "../api/adminAPI";
import {get} from "../utils/utils";

function Menu() {
    const navigate = useNavigate();
    const [admin = {
        administrator: {},
        adminId: '',
        email: '',
        restaurant: {}
    }, setAdmin] = useState(get('admin-info'));
    const [restaurant = {
        name: '',
        location: '',
        locationZone: {
            name: ''
        },
        deliveryZones: [],
        menu: {}
    }, setRestaurant] = useState(admin.restaurant);
    const [menu = {
        menuId: '',
        categories: []
    }, setMenu] = useState(restaurant.menu);
    const [categories = {
        categoryId: '',
        category: '',
        foodList: {
            foodId: '',
            name: '',
            description: '',
            price: null
        }
    }, setCategories] = useState(menu.categories);
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
        console.log(food)
    }

    function handleSubmit() {
        addFood(category, food)
            .then(() => {
                fetchMenu(restaurant)
                    .then(response => {
                        console.log(response)
                        setRestaurant(prevState => {
                            return {
                                ...prevState,
                                menu: response
                            }
                        });
                        setAdmin(prevState => {
                            return {
                                ...prevState,
                                restaurant: restaurant
                            }
                        })
                    })
                    .catch(error => {
                        console.log(error)
                    })
            })
            .catch(error => {
                setError(error.response.data.message)
            });
    }

    useEffect(() => {
        if (!admin)
            navigate("/login");
    }, [])


    return (
        <div>
            <h1> Hello, back {restaurant.name} </h1>

            <h1>Add a new food!</h1>

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
            <br/>

            <input
                name={'name'}
                type={'text'}
                placeholder={'Name...'}
                onChange={handleChange}/>
            <br/>

            <input
                name={'description'}
                type={'text'}
                placeholder={'Description...'}
                onChange={handleChange}/>
            <br/>

            <input
                name={'price'}
                type={'number'}
                placeholder={'Price...'}
                onChange={handleChange}/>

            <button
                onClick={handleSubmit}>
                Add new food!
            </button>
            <br/>
            <h1>{error}</h1>

            <h1>
                Menu
            </h1>
            <ul>
                {
                    categories.map(cat => {
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

export default Menu;