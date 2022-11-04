import React from "react";

type PageWrapperProps = {
    children: React.ReactNode;
};

const wrapperStyle = {
    padding: "25px"
}

const PageWrapper = (props: PageWrapperProps) => {
    
    return (
        <div style={wrapperStyle}>
            {props.children}
        </div>
    );
}

export default PageWrapper;