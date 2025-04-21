// mockSeasonalTemperature.ts

export const mockSeasonalTemperature = {
    code: 0,
    message: "success",
    data: [
        {
            season: "春",
            year: 2023,
            avgTemp: { value: 14.2, unit: "°C" }
        },
        {
            season: "夏",
            year: 2023,
            avgTemp: { value: 28.5, unit: "°C" }
        },
        {
            season: "秋",
            year: 2023,
            avgTemp: { value: 18.3, unit: "°C" }
        },
        {
            season: "冬",
            year: 2023,
            avgTemp: { value: 5.7, unit: "°C" }
        }
    ]
}
