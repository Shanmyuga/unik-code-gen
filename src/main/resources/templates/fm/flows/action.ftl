import { ${actionMethodName} } from '${serviceFileName}';

export const ${actionConstant} = '${actionConstant}';

export const actions = {
    ${actionMethodName}: (args) => {
        return {
            type: `${actionType}`,
            payload: {
              promise: ${actionMethodName}(args)
            }
        };
    }
};