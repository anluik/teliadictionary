import Axios from "axios";
import { IFetchResponse } from '@/types/IFetchResponse';

export abstract class BaseService<IDomain> {
    private static axios = Axios.create({
        baseURL: "http://localhost:8081/",
        headers: {
            common: {
                "Content-Type": "application/json"
            }
        }
    });

    private static getStatusResponseText(statusCode: number): string {
        switch (statusCode) {
        case 201:
            return "Success"
        case 204:
            return "Success"
        case 400:
            return "Please check if the data you provided is correct"
        case 404:
            return "Fill the search :)"
        case 409:
            return "Resource already exists"
        case 418:
            return "No, you are a teapot!"
        case 500:
            return "Server side error. Please try again later."
        default:
            return "Something went terribly wrong."
        }
    }

    private static dealWithError(error: Error) {
        const regex = /\d\d\d/g;
        const found = error.message.match(regex);
        const statusCode = parseInt(found![0]);
        return {
            statusCode: statusCode,
            errorMessage: this.getStatusResponseText(statusCode)
        };
    }

    static async getAll<IDomain>(url: string, condition?: string | null): Promise<IFetchResponse<IDomain[]>> {
        try {
            const response = await this.axios.get<IDomain[]>(url, { params: { term: condition } });
            if (response.status === 200) {
                return {
                    statusCode: response.status,
                    data: response.data
                }
            }

            const otherResonseStatus = this.getStatusResponseText(response.status);
            return {
                statusCode: response.status,
                errorMessage: otherResonseStatus
            };
        } catch (error) {
            return this.dealWithError(error);
        }
    }

    static async getOne<IDomain>(url: string): Promise<IFetchResponse<IDomain>> {
        try {
            const response = await this.axios.get<IDomain>(url);
            if (response.status === 200) {
                return {
                    statusCode: response.status,
                    data: response.data
                }
            }
            return {
                statusCode: response.status,
                errorMessage: this.getStatusResponseText(response.status)
            }
        } catch (error) {
            return this.dealWithError(error);
        }
    }

    static async delete<IDomain>(url: string): Promise<IFetchResponse<string>> {
        try {
            const response = await this.axios.delete<IDomain>(url);

            return {
                statusCode: response.status,
                errorMessage: this.getStatusResponseText(response.status)
            }
        } catch (error) {
            return this.dealWithError(error);
        }
    }

    static async create<IDomain>(url: string, data: IDomain | IDomain[]): Promise<IFetchResponse<string>> {
        try {
            const response = await this.axios.post<IDomain | IDomain[]>(url, data);
            if (response.status === 201) {
                return {
                    statusCode: response.status
                }
            }
            return {
                statusCode: response.status,
                errorMessage: this.getStatusResponseText(response.status)
            }
        } catch (error) {
            return this.dealWithError(error);
        }
    }

    static async update<IDomain>(url: string, data: IDomain): Promise<IFetchResponse<string>> {
        try {
            const response = await this.axios.put<IDomain>(url, data);
            if (response.status === 204) {
                return {
                    statusCode: response.status
                }
            }
            return {
                statusCode: response.status,
                errorMessage: this.getStatusResponseText(response.status)
            }
        } catch (error) {
            return this.dealWithError(error);
        }
    }

    static async getAllFuzzy<IDomain>(url: string): Promise<IFetchResponse<IDomain[]>> {
        try {
            const response = await this.axios.get<IDomain[]>(url);
            if (response.status === 200) {
                return {
                    statusCode: response.status,
                    data: response.data
                }
            }

            const otherResonseStatus = this.getStatusResponseText(response.status);
            return {
                statusCode: response.status,
                errorMessage: otherResonseStatus
            };
        } catch (error) {
            return this.dealWithError(error);
        }
    }
}
