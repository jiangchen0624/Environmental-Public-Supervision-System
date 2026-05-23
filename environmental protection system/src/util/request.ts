const BASE_URL = '/api'

export interface ApiResult {
  code: number
  msg: string
  [key: string]: unknown
}

/**
 * 发送 POST 请求
 */
export async function post(path: string, data: Record<string, unknown>): Promise<ApiResult> {
  const res = await fetch(`${BASE_URL}${path}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
  if (!res.ok) throw new Error(`HTTP ${res.status}`)
  return res.json()
}

/**
 * 发送 GET 请求
 */
export async function get(path: string, params?: Record<string, string>): Promise<ApiResult> {
  let url = `${BASE_URL}${path}`
  if (params) {
    const qs = new URLSearchParams(params).toString()
    if (qs) url += (path.includes('?') ? '&' : '?') + qs
  }
  const res = await fetch(url)
  if (!res.ok) throw new Error(`HTTP ${res.status}`)
  return res.json()
}
