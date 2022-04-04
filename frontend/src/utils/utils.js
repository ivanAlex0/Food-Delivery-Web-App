export function get(key) {
    const info = localStorage.getItem(key)
    return JSON.parse(info)
}

export default {get};