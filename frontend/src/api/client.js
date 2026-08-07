const BASE = '/api'

async function get(path) {
    const res = await fetch(`${BASE}${path}`)
    if (!res.ok) throw new Error(`Server error: ${res.status}`)
    return res.json()
}

async function post(path) {
    const res = await fetch(`${BASE}${path}`, {method: "POST",})
    if (!res.ok) {
        throw new Error(`Server error: ${res.status}`)
    }
}

export const api = {
    getSites: () => get('/site'),
    getSiteById: (id) => get(`/site/${id}`),
    getSummary: (siteId) => get(`/dashboard/${siteId}/summary`),
    getPages: (siteId, page) => get(`/dashboard/${siteId}/pages?page=${page}&size=20`),
    getNoH1: (siteId) => get(`/dashboard/${siteId}/no-h1`),
    getRedirects: (siteId) => get(`/dashboard/${siteId}/redirects`),
    getErrors: (siteId) => get(`/dashboard/${siteId}/errors`),
    getChanges: (siteId, page) => get(`/dashboard/${siteId}/changes?page=${page}&size=20`),
    runAudit: (siteId) => post(`/site/${siteId}/check`)}