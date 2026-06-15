export function formatDate(dt) {
  if (!dt) return '—'
  const date = new Date(dt)

  const formattedDate = date.toLocaleDateString('uk-UA')
  const formattedTime = "(" + date.toLocaleTimeString('uk-UA') +")"

  return `${formattedDate} ${formattedTime}`
}