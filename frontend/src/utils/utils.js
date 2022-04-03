export function get(key) {
    const admin_info = localStorage.getItem(key)
    return JSON.parse(admin_info)
}

export default get;