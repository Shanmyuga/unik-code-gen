
import ${reducer.reducerName?uncap_first} from '${reducer.reducerRelativePath}';
import ${reducer.actionConstant} from '${reducer.actionRelativePath}';

describe('${reducer.reducerName?uncap_first}', () => {

    describe(' should return the initial state', () => {
  it('is correct', () => {
    const action = { type: 'default' };
    const initialState = { data : null };

    expect(${reducer.reducerName?uncap_first}(undefined, action)).toEqual(initialState);
  });
});

  describe('Unit Test for checking ${reducer.actionConstant}', () => {
  it('returns the correct state', () => {

    const action = { 
        type: '${reducer.actionConstant}', 
        payload: 1
     };
    const expectedState = { data: 1 };

    expect(${reducer.reducerName?uncap_first}(undefined, action)).toEqual(expectedState);
  });
});
});