import React from 'react'

const ChatRoomListing = () => {
  return (
    <div className='flex flex-col text-left bg-[#863dff] text-[#070a26] opacity-90 w-full h-full rounded-3xl mx-0 p-10 gap-2 z-5 border-1'>
        <h3 className='text-2xl text-[#cbff2e]'>Live rooms.</h3>
        <div className='container text-2xl pt-10 flex flex-col gap-5 bg-opacity-100 my-10 py-4 px-10 rounded-lg h-full overflow-y-scroll'>
            <ul className='flex flex-col text-5xl text-[#cbff2e] gap-5'>
            <div className='flex justify-between'>
                <p className='hover:cursor-default'>Fight Club</p>
                <p className='hover:cursor-pointer'>+</p>
            </div>
            <div className='flex justify-between'>
                <p className='hover:cursor-default'>Anime Fanatics</p>
                <p className='hover:cursor-pointer'>+</p>
            </div>
            <div className='flex justify-between'>
                <p className='hover:cursor-default'>Football Fandom</p>
                <p className='hover:cursor-pointer'>+</p>
            </div>
            <div className='flex justify-between'>
                <p className='hover:cursor-default'>General</p>
                <p className='hover:cursor-pointer'>+</p>
            </div>
            </ul>
        </div>
        <p className='absolute bottom-5 right-5 text-[#cbff2e]'><span className='animate-ping duration-2000'> 8</span> Rooms Live</p>
    </div>
  )
}

export default ChatRoomListing