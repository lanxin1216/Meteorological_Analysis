declare namespace API {
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

  type getByDateUsingGETParams = {
    /** day */
    day: number;
    /** month */
    month: number;
    /** type */
    type: string;
    /** year */
    year: number;
  };

  type getByDayUsingGETParams = {
    /** month */
    month: number;
    /** type */
    type: string;
    /** year */
    year: number;
  };

  type getByMonthUsingGETParams = {
    /** type */
    type: string;
    /** year */
    year: number;
  };

  type uploadAndImportUsingPOSTParams = {
    /** year */
    year: number;
  };

  type WeatherPoint = {
    label?: string;
    value?: number;
  };
}
