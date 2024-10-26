export interface PromptResult {
    resultCode: PromptResultCode;
    prompt: string; // user
    init: string;   // assistant
}

export type PromptResultCode =
    'ok'
    | 'AICfgNotSet'
    | 'tableNotSet'
    | 'tableNotFound'
    | 'promptFileNotFound';


export interface CheckJsonResult {
    resultCode: CheckJsonResultCode;
    table: string;
    jsonResult: string; // or err for ParseJsonError
}

export type CheckJsonResultCode =
    'ok'
    | 'tableNotFound'
    | 'JsonNotFound'
    | 'ParseJsonError';
