declare namespace API {
  type BaseResponseExtremeTemperatureDTO_ = {
    code?: number;
    data?: ExtremeTemperatureDTO;
    message?: string;
  };

  type BaseResponseListCorrelationDTO_ = {
    code?: number;
    data?: CorrelationDTO[];
    message?: string;
  };

  type BaseResponseListMonthAvgWindDTO_ = {
    code?: number;
    data?: MonthAvgWindDTO[];
    message?: string;
  };

  type BaseResponseListSeasonTemperatureDTO_ = {
    code?: number;
    data?: SeasonTemperatureDTO[];
    message?: string;
  };

  type BaseResponseListStrongWindDaysDTO_ = {
    code?: number;
    data?: StrongWindDaysDTO[];
    message?: string;
  };

  type BaseResponseListTempDewPointDiffDTO_ = {
    code?: number;
    data?: TempDewPointDiffDTO[];
    message?: string;
  };

  type BaseResponseListTemperatureTrendDTO_ = {
    code?: number;
    data?: TemperatureTrendDTO[];
    message?: string;
  };

  type BaseResponseListWeatherDataVO_ = {
    code?: number;
    data?: WeatherDataVO[];
    message?: string;
  };

  type BaseResponseListWeatherPoint_ = {
    code?: number;
    data?: WeatherPoint[];
    message?: string;
  };

  type BaseResponseListWeatherTrendDTO_ = {
    code?: number;
    data?: WeatherTrendDTO[];
    message?: string;
  };

  type BaseResponseObject_ = {
    code?: number;
    data?: Record<string, any>;
    message?: string;
  };

  type BaseResponseString_ = {
    code?: number;
    data?: string;
    message?: string;
  };

  type CorrelationDTO = {
    correlationValue?: number;
    month?: number;
    year?: number;
  };

  type ExtremeTemperatureDTO = {
    maxTemperature?: number;
    maxTemperatureTime?: string;
    minTemperature?: number;
    minTemperatureTime?: string;
    year?: number;
  };

  type getByDayUsingGETParams = {
    /** day */
    day: number;
    /** month */
    month: number;
    /** type */
    type: string;
    /** year */
    year: number;
  };

  type getByMonthUsingGETParams = {
    /** month */
    month: number;
    /** type */
    type: string;
    /** year */
    year: number;
  };

  type getByYearUsingGETParams = {
    /** type */
    type: string;
    /** year */
    year: number;
  };

  type getExtremeTemperaturesUsingGETParams = {
    /** year */
    year: number;
  };

  type getMonthlyTemperatureTrendUsingGETParams = {
    /** year */
    year: number;
  };

  type getMonthlyWindSpeedUsingGETParams = {
    /** year */
    year: number;
  };

  type getSeasonalTemperatureUsingGETParams = {
    /** year */
    year: number;
  };

  type getStrongWindDaysUsingGETParams = {
    /** threshold */
    threshold?: number;
  };

  type getTempDewPointDistributionUsingGETParams = {
    /** year */
    year: number;
  };

  type getTempPressureCorrelationUsingGETParams = {
    /** year */
    year: number;
  };

  type getTop500ByYearUsingGETParams = {
    /** year */
    year: number;
  };

  type getYearlyPressureTrendUsingGETParams = {
    /** endYear */
    endYear: number;
    /** startYear */
    startYear: number;
  };

  type MonthAvgWindDTO = {
    avgSpeed?: number;
    month?: number;
  };

  type SeasonTemperatureDTO = {
    avgTemp?: number;
    season?: string;
    year?: number;
  };

  type StrongWindDaysDTO = {
    days?: number;
    year?: number;
  };

  type TempDewPointDiffDTO = {
    recordCount?: number;
    tempDiffRange?: number;
  };

  type TemperatureTrendDTO = {
    avgTemp?: number;
    month?: number;
  };

  type uploadAndImportUsingPOSTParams = {
    /** year */
    year: number;
  };

  type WeatherDataVO = {
    dataTime?: string;
    day?: number;
    dewPoint?: number;
    hour?: number;
    month?: number;
    pressure?: number;
    temperature?: number;
    windDirection?: number;
    windSpeed?: number;
    year?: number;
  };

  type WeatherPoint = {
    label?: string;
    value?: number;
  };

  type WeatherTrendDTO = {
    avgValue?: number;
    year?: number;
  };
}
