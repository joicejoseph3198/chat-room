export const  InputTextField = (props) => {
    const {title, placeholder, onClickHandler, onChangeHandler, valueStore, name} = props;
    return(
        <div className="pb-2 text-xs">
            <h3 className="pb-2">{title}</h3>
            <input type="text" placeholder={placeholder} value={valueStore} name={name} onChange={onChangeHandler}
                className="bg-transparent text-black w-fit focus:outline-none px-4 py-2 text-xs font-semibold border-2 rounded-md" 
            />
        </div>
    )
};