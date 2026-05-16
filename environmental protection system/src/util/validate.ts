/** 校验手机号 */
export function validatePhone(phone: string): string | null {
  if (!phone) return '请输入手机号'
  if (!/^1[3-9]\d{9}$/.test(phone)) return '请输入正确的手机号'
  return null
}

/** 校验姓名 */
export function validateName(name: string): string | null {
  if (!name) return '请输入姓名'
  return null
}

/** 校验密码 */
export function validatePassword(password: string): string | null {
  if (!password) return '请输入密码'
  if (password.length < 6) return '密码长度不能少于6位'
  return null
}

/** 校验确认密码 */
export function validateConfirmPassword(password: string, confirmPassword: string): string | null {
  if (!confirmPassword) return '请确认密码'
  if (password !== confirmPassword) return '两次输入密码不一致'
  return null
}
