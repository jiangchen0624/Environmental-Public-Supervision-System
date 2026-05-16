const BASE_URL = '/api'

interface ApiResult {
  code: number
  msg: string
  [key: string]: unknown
}
/**
 * 发送 POST 请求
 * @param path - 请求路径（相对于 BASE_URL）
 * @param data - 请求体数据
 * @returns 返回 API 响应结果
 */
export async function post(path: string, data: Record<string, unknown>): Promise<ApiResult> {
  const res = await fetch(`${BASE_URL}${path}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
  return res.json()
}
