import { ${actionConstant} } from '${actionFileName}';

const initialState = {
    data: null
};

export default function ${reducerName}(state = initialState, action) {
  switch (action.type) {
    case `${reducerType}`: {
      return {
        ...state,
        data: action.payload
      };

      break;
    }
    default:
      return state;
  }
}
