declare namespace API {
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

  type getTop500ByYearUsingGETParams = {
    /** year */
    year: number;
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
}
