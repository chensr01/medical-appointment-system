// Generates a random value from an enum
export function randomEnum<T extends Object>(anEnum: T): T[keyof T] {
  const enumValues = Object.values(anEnum);
  const randomIndex = Math.floor(Math.random() * enumValues.length);
  return enumValues[randomIndex] as T[keyof T];
}
