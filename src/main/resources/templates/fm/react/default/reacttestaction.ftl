import configureMockStore from "redux-mock-store";
const mockStore = configureMockStore();

const ${action.actionName} = () => ({ type: '${action.actionConstant}',payload:1 });

it('should dispatch action', () => {
  const initialState = {}
  const store = mockStore(initialState)
  store.dispatch(${action.actionName}())
  const actions = store.getActions()
  const expectedPayload = { 
  type: '${action.actionConstant}',
  payload:1
  }
  expect(actions).toEqual([expectedPayload])
});