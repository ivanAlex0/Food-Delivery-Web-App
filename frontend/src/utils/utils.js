import starters from '../res/starters.jpg'
import main_dish from '../res/main_dish.jpg'
import garnish from '../res/garnish.jpg'
import soup from '../res/soup.jpg'
import dessert from '../res/dessert.jpg'
import beverages from '../res/beverages.jpg'

export function get(key) {
    const info = localStorage.getItem(key)
    return JSON.parse(info)
}

export const foodList = [beverages, starters, soup, main_dish, garnish, dessert];

export default {get, foodList};
