/**
 * 空气质量等级 → 中文名称
 */
export function levelName(lv: number): string {
  return ['', '优', '良', '轻度污染', '中度污染', '重度污染'][lv] || ''
}

/**
 * 等级 → Element Plus Tag 类型
 */
export function levelTagType(lv: number): 'success' | 'primary' | 'warning' | 'danger' | 'info' {
  const map: Record<number, 'success' | 'primary' | 'warning' | 'danger'> = {
    1: 'success',
    2: 'primary',
    3: 'warning',
    4: 'danger',
    5: 'danger',
  }
  return map[lv] || 'info'
}

/**
 * Report 状态 → Element Plus Tag 类型
 */
export function statusTagType(s: string): 'danger' | 'warning' | 'success' | 'info' {
  const map: Record<string, 'danger' | 'warning' | 'success'> = {
    待指派: 'danger',
    已指派: 'warning',
    已检测: 'success',
  }
  return map[s] || 'info'
}
